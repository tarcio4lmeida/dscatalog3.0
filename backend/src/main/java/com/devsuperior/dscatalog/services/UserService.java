package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BCryptPasswordEncoder passwordEnconder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Transactional(readOnly=true)
    public Page<UserDTO> findAll(PageRequest pageRequest){
        Page<User> list =  userRepository.findAll(pageRequest);
        return  list.map(UserDTO::new);
    }

    @Transactional(readOnly=true)
    public UserDTO findById(long id){
        Optional<User> obj = userRepository.findById(id);
        User entity  = obj.orElseThrow(() -> new ResourcesNotFoundException("Entity not found"));
        return  new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEnconder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return  new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        // GetOne -> ele nao vai ao banco, somente instancia um objeto de uma determinada entity com o valor passado
        try {
            User entity = userRepository.getOne(id);
            copyDtoToEntity(dto, entity);

            userRepository.save(entity);
            return new UserDTO(entity);
        }
        catch(EntityNotFoundException e ) {
            throw new ResourcesNotFoundException("Id not found " + id);
        }
    }

    public void delete(long id) {
        try {
            userRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e ) {
            throw new ResourcesNotFoundException("Id not found " + id);
        }
        catch(DataIntegrityViolationException e ) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()) {
            Role role =  roleRepository.getOne(roleDto.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found: " + email);
                    return new UsernameNotFoundException("Email not found");
                });
    }
}