#!/bin/bash

set -e

#################################################
# Hotel Management System Deployment Script
#################################################

APP_NAME="hotel-management-system"
DEPLOY_DIR="/opt/hotel-management"
BACKUP_DIR="$DEPLOY_DIR/backups"
LOG_DIR="$DEPLOY_DIR/logs"

JAR_NAME="app.jar"

PORT=8081

SPRING_PROFILE=prod

HEALTH_URL="http://localhost:${PORT}/actuator/health"

#################################################

echo "=========================================="
echo "Hotel Management System Deployment"
echo "=========================================="

#################################################
# Check Java
#################################################

if ! command -v java >/dev/null 2>&1; then
    echo "Java is not installed."
    exit 1
fi

echo "Java Version"
java -version

#################################################
# Create Directories
#################################################

mkdir -p "$DEPLOY_DIR"
mkdir -p "$BACKUP_DIR"
mkdir -p "$LOG_DIR"

#################################################
# Verify artifact
#################################################

if [ ! -f "/tmp/hotel-app/$JAR_NAME" ]; then
    echo "Artifact not found."
    exit 1
fi

#################################################
# Backup current version
#################################################

if [ -f "$DEPLOY_DIR/$JAR_NAME" ]; then

    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

    cp "$DEPLOY_DIR/$JAR_NAME" \
       "$BACKUP_DIR/$JAR_NAME.$TIMESTAMP"

    echo "Backup created."
fi

#################################################
# Stop existing application
#################################################

PID=$(pgrep -f "$JAR_NAME")

if [ ! -z "$PID" ]; then

    echo "Stopping application PID=$PID"

    kill "$PID"

    sleep 8

fi

#################################################
# Copy new artifact
#################################################

cp "/tmp/hotel-app/$JAR_NAME" \
   "$DEPLOY_DIR/"

#################################################
# Start application
#################################################

echo "Starting application..."

nohup java \
    -jar "$DEPLOY_DIR/$JAR_NAME" \
    --server.port=$PORT \
    --spring.profiles.active=$SPRING_PROFILE \
    > "$LOG_DIR/application.log" 2>&1 &

NEW_PID=$!

echo "PID = $NEW_PID"

#################################################
# Wait for startup
#################################################

echo "Waiting for application..."

sleep 20

#################################################
# Health Check
#################################################

STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$HEALTH_URL")

if [ "$STATUS" = "200" ]; then

    echo ""
    echo "Deployment Successful"
    echo ""
    echo "Application URL"
    echo "http://localhost:$PORT"
    echo ""
    echo "Swagger"
    echo "http://localhost:$PORT/swagger-ui/index.html"
    echo ""
    echo "Health"
    echo "$HEALTH_URL"
    echo ""

else

    echo "Deployment Failed"

    echo "Rolling back..."

    kill "$NEW_PID" || true

    LAST_BACKUP=$(ls -t "$BACKUP_DIR" | head -1)

    if [ ! -z "$LAST_BACKUP" ]; then

        cp "$BACKUP_DIR/$LAST_BACKUP" \
           "$DEPLOY_DIR/$JAR_NAME"

        nohup java \
            -jar "$DEPLOY_DIR/$JAR_NAME" \
            --server.port=$PORT \
            > "$LOG_DIR/application.log" 2>&1 &

        echo "Rollback completed."

    fi

    exit 1

fi

#################################################
# Cleanup old backups
#################################################

find "$BACKUP_DIR" -type f -mtime +15 -delete

#################################################

echo "=========================================="
echo "Deployment Finished"
echo "Application : $APP_NAME"
echo "PID         : $NEW_PID"
echo "Logs        : $LOG_DIR/application.log"
echo "=========================================="