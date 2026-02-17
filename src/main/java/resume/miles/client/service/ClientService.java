package resume.miles.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.client.dto.ClientDto;
import resume.miles.client.entity.ClientEntity;
import resume.miles.client.mapper.ClientMapper;
import resume.miles.client.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {
      private final ClientRepository clientRepository;
        public ClientDto createClient(ClientDto clientDto) {

        // 1️⃣ Convert DTO → Entity
        ClientEntity clientEntity = ClientMapper.toEntity(clientDto);

        // 2️⃣ Set default status if null
        if (clientEntity.getStatus() == null) {
            clientEntity.setStatus(1);
        }

        // 3️⃣ Save
        ClientEntity savedClient = clientRepository.save(clientEntity);

        // 4️⃣ Convert back to DTO
        return ClientMapper.toDto(savedClient);
    }
    
     public List<ClientDto> getAllClients() {

        return clientRepository.findAll()
                .stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ClientDto> getActiveClients() {

        return clientRepository.findByStatus(1)
                .stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

}
