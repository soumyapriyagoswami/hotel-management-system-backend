package com.hotel.hotel_management_system.service;

import com.hotel.hotel_management_system.dto.GuestDTO;
import com.hotel.hotel_management_system.exception.ResourceNotFoundException;
import com.hotel.hotel_management_system.exception.ValidationException;
import com.hotel.hotel_management_system.model.Guest;
import com.hotel.hotel_management_system.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuestService {

    private final GuestRepository guestRepository;

    public List<Guest> getAllGuests() {
        log.info("Fetching all guests");
        return guestRepository.findAll();
    }

    public Guest getGuestById(Long id) {
        log.info("Fetching guest with id: {}", id);
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + id));
    }

    public Guest getGuestByEmail(String email) {
        log.info("Fetching guest by email: {}", email);
        return guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with email: " + email));
    }

    public List<Guest> searchGuests(String searchTerm) {
        log.info("Searching guests with term: {}", searchTerm);
        return guestRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm);
    }

    public List<Guest> getVIPGuests() {
        log.info("Fetching VIP guests");
        return guestRepository.findByIsVipTrue();
    }

    public List<Guest> getCurrentGuests() {
        log.info("Fetching current guests");
        return guestRepository.findCurrentGuests();
    }

    @Transactional
    public Guest createGuest(GuestDTO guestDTO) {
        log.info("Creating new guest: {}", guestDTO.getEmail());
        
        // Check if guest already exists
        Optional<Guest> existingGuest = guestRepository.findByEmail(guestDTO.getEmail());
        if (existingGuest.isPresent()) {
            throw new ValidationException("Guest with email " + guestDTO.getEmail() + " already exists");
        }
        
        Guest guest = new Guest();
        guest.setFirstName(guestDTO.getFirstName());
        guest.setLastName(guestDTO.getLastName());
        guest.setEmail(guestDTO.getEmail());
        guest.setPhoneNumber(guestDTO.getPhoneNumber());
        guest.setAddress(guestDTO.getAddress());
        guest.setCity(guestDTO.getCity());
        guest.setCountry(guestDTO.getCountry());
        guest.setPostalCode(guestDTO.getPostalCode());
        guest.setDateOfBirth(guestDTO.getDateOfBirth());
        guest.setIdCardNumber(guestDTO.getIdCardNumber());
        guest.setEmergencyContactName(guestDTO.getEmergencyContactName());
        guest.setEmergencyContactPhone(guestDTO.getEmergencyContactPhone());
        guest.setSpecialRequirements(guestDTO.getSpecialRequirements());
        guest.setIsVip(guestDTO.getIsVip() != null && guestDTO.getIsVip());
        guest.setTotalStays(0);
        
        return guestRepository.save(guest);
    }

    @Transactional
    public Guest updateGuest(Long id, GuestDTO guestDTO) {
        log.info("Updating guest with id: {}", id);
        Guest guest = getGuestById(id);
        
        guest.setFirstName(guestDTO.getFirstName());
        guest.setLastName(guestDTO.getLastName());
        guest.setEmail(guestDTO.getEmail());
        guest.setPhoneNumber(guestDTO.getPhoneNumber());
        guest.setAddress(guestDTO.getAddress());
        guest.setCity(guestDTO.getCity());
        guest.setCountry(guestDTO.getCountry());
        guest.setPostalCode(guestDTO.getPostalCode());
        guest.setDateOfBirth(guestDTO.getDateOfBirth());
        guest.setIdCardNumber(guestDTO.getIdCardNumber());
        guest.setEmergencyContactName(guestDTO.getEmergencyContactName());
        guest.setEmergencyContactPhone(guestDTO.getEmergencyContactPhone());
        guest.setSpecialRequirements(guestDTO.getSpecialRequirements());
        guest.setIsVip(guestDTO.getIsVip());
        
        return guestRepository.save(guest);
    }

    @Transactional
    public void deleteGuest(Long id) {
        log.info("Deleting guest with id: {}", id);
        Guest guest = getGuestById(id);
        guestRepository.delete(guest);
    }

    @Transactional
    public void incrementTotalStays(Long guestId) {
        Guest guest = getGuestById(guestId);
        guest.setTotalStays(guest.getTotalStays() + 1);
        guestRepository.save(guest);
    }

    public Long getTotalGuestsCount() {
        return guestRepository.countTotalGuests();
    }

    public List<Object[]> getGuestCityStats() {
        return guestRepository.countGuestsByCity();
    }
}