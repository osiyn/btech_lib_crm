package ru.libcrm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.libcrm.model.Reader;
import ru.libcrm.model.exception.ReaderNotFoundException;
import ru.libcrm.repository.ReaderRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReaderService {

    private final ReaderRepository readerRepository;

    @Transactional
    public Reader registerReader(Reader reader) {
        return readerRepository.save(reader);
    }

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public Reader getReaderById(UUID id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new ReaderNotFoundException(id));
    }
}
