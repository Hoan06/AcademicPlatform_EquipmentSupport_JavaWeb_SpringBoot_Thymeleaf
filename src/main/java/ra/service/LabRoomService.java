package ra.service;

import ra.model.entity.LabRoom;

import java.util.List;

public interface LabRoomService {
    List<LabRoom> getAll();
    LabRoom getById(Long id);
}
