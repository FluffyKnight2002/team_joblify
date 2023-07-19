package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.entity.Address;

import java.util.List;

public interface AddressService {
    Address addAddress(Address address);
    List<Address> selectAllAddress();
    void removeAddress(long id);
    List<Address> findByNameContainingIgnoreCase(String term);
    Address findByName(String name);
}
