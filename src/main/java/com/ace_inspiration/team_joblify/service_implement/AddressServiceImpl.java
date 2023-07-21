package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.entity.Address;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.service.AddressService;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    @Override
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> selectAllAddress() {

        return addressRepository.findAll();
    }

    @Override
    public void removeAddress(long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public List<Address> findByNameContainingIgnoreCase(String term) {
        return addressRepository.findByNameContainingIgnoreCase(term);
    }

    @Override
    public Address findByName(String name) {
        return addressRepository.findByName(name).orElse(null);
    }

    @Override
    public Address checkAndSetAddress(String newAddress) {
        Address address = new Address();
        if(addressRepository.findByName(newAddress).isEmpty()) {
            autoFillAddress(newAddress);
        }
        address = convertAddress(newAddress);
        return address;
    }

    @Override
    public void autoFillAddress(String newName) {
        Address newAddress = Address.builder()
                .name(newName)
                .build();
        addressRepository.save(newAddress);
    }

    @Override
    public Address convertAddress(String addressName) {

        return addressRepository.findByName(addressName).orElse(null);
    }
}
