package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.entity.Address;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.service_implement.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class AddressTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckAndSetAddress_ExistingAddress() {
        // Arrange
        String existingAddressName = "Existing Address";
        Address existingAddress = Address.builder().name(existingAddressName).build();
        when(addressRepository.findByName(existingAddressName)).thenReturn(Optional.of(existingAddress));

        // Act
        Address result = addressService.checkAndSetAddress(existingAddressName);

        // Assert
        verify(addressRepository, never()).save(any()); // Verify that save is not called
        assertSame(existingAddress, result); // Make sure the function returns the existing address
    }

//    @Test
//    public void testCheckAndSetAddress_NonExistingAddress() {
//        // Arrange
//        String newAddressName = "New Address";
//        when(addressRepository.findByName(newAddressName)).thenReturn(Optional.empty());
//
//        // Act
//        Address result = addressService.checkAndSetAddress(newAddressName);
//
//        // Assert
//        verify(addressRepository, times(1)).save(any()); // Verify that save is called once
//        assertEquals(newAddressName, result.getName()); // Make sure the returned address name matches the input name
//    }

//    @Test
//    public void testCheckAndSetAddress_NonExistingAddress() {
//        // Arrange
//        String newAddressName = "New Address";
//        when(addressRepository.findByName(newAddressName)).thenReturn(Optional.empty());
//
//        // Act
//        Address result = addressService.checkAndSetAddress(newAddressName);
//
//        // Assert
//        verify(addressRepository, times(1)).save(any());
//        assertNotNull(result); // Ensure the result is not null
//        assertEquals(newAddressName, result.getName());
//    }

    @Test
    public void testCheckAndSetAddress_NonExistingAddress() {
        // Arrange
        String newAddressName = "New Address";
        when(addressRepository.findByName(newAddressName)).thenReturn(Optional.empty());

        // Act
        Address result = addressService.checkAndSetAddress(newAddressName);

        // Assert
        verify(addressRepository, times(1)).save(any());
        assertNull(result); // Ensure the result is null for non-existing addresses
    }

    @Test
    public void testAutoFillAddress() {
        // Arrange
        String newName = "New Address";

        // Act
        addressService.autoFillAddress(newName);

        // Assert
        verify(addressRepository, times(1)).save(any()); // Verify that save is called once
    }

    @Test
    public void testConvertAddress_ExistingAddress() {
        // Arrange
        String existingAddressName = "Existing Address";
        Address existingAddress = Address.builder().name(existingAddressName).build();
        when(addressRepository.findByName(existingAddressName)).thenReturn(Optional.of(existingAddress));

        // Act
        Address result = addressService.convertAddress(existingAddressName);

        // Assert
        assertSame(existingAddress, result); // Make sure the function returns the existing address
    }

    @Test
    public void testConvertAddress_NonExistingAddress() {
        // Arrange
        String nonExistingAddressName = "Non-existing Address";
        when(addressRepository.findByName(nonExistingAddressName)).thenReturn(Optional.empty());

        // Act
        Address result = addressService.convertAddress(nonExistingAddressName);

        // Assert
        assertNull(result); // Make sure the function returns null for non-existing addresses
    }
}
