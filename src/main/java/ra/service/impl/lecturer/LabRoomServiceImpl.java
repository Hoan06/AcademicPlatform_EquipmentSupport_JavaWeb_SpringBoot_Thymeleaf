package ra.service.impl.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.LabRoom;
import ra.repository.lecturer.LabRoomRepository;
import ra.service.LabRoomService;

import java.util.List;

@Service
public class LabRoomServiceImpl implements LabRoomService {
    @Autowired
    private LabRoomRepository labRoomRepository;

    @Override
    public List<LabRoom> getAll() {
        return labRoomRepository.findAll();
    }

    @Override
    public LabRoom getById(Long id) {
        return labRoomRepository.findById(id).get();
    }
}
