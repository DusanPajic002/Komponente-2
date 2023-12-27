package com.example.mapper;

import com.example.domain.Client;
import com.example.domain.Manager;
import com.example.domain.User;
import com.example.dto.ManagerCreateDto;
import com.example.dto.ManagerDto;
import com.example.dto.UpdatePermissionDto;
import com.example.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {

    public ManagerDto managerToManagerDto(Manager manager){
        return new ManagerDto(manager.getId(), manager.getHallName(), manager.getUser().getUsername(),
                manager.getUser().getEmail(),manager.getUser().getFirstName(),manager.getUser().getLastName());
    }
    public UserDto managerToUserDto(Manager manager){
        return new UserDto(manager.getUser().getUsername(),manager.getUser().getEmail(),manager.getUser().getFirstName(),manager.getUser().getLastName());
    }

    public Manager managerCreateDtoToManager(ManagerCreateDto managerCreateDto){
        User user = new User(managerCreateDto.getUserDto().getUsername(),managerCreateDto.getUserDto().getPassword(),managerCreateDto.getUserDto().getEmail(),
                managerCreateDto.getUserDto().getDateOfBirth(),managerCreateDto.getUserDto().getFirstName(),managerCreateDto.getUserDto().getLastName(),managerCreateDto.getUserDto().isPermission());
        return new Manager(managerCreateDto.getId(),managerCreateDto.getHallName(),managerCreateDto.getStartDate(),user);
    }

    public Manager updatePermissionDtoToManager(Manager manager, UpdatePermissionDto updatePermissionDto){
        manager.getUser().setPermission(updatePermissionDto.isPermission());
        return manager;
    }

}
