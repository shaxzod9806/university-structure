package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.AddressRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressRepository addressRepository;


    @GetMapping
    public List<Address> getAll() {
        List<Address> addressList = addressRepository.findAll();
        return addressList;
    }

    @GetMapping(value = "/{id}")
    public Address getById(@PathVariable Integer id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            return address;
        } else {
            return new Address();
        }
    }

    @PostMapping
    public String add(@RequestBody Address address){
        addressRepository.save(address);
        return "Address added";
    }

    @PutMapping(value = "/{id}")
    public String edit(@PathVariable Integer id, @RequestBody Address comingAddress) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setCity(comingAddress.getCity());
            address.setDistrict(comingAddress.getDistrict());
            address.setStreet(comingAddress.getStreet());
            addressRepository.save(address);
            return "Address saved";
        } else {
            return "Address not found";
        }
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            addressRepository.deleteById(id);
            return "Address deleted";
        } else {
            return "Address not found";
        }
    }

}
