package com.ListOnGo.ServerListOnGo.Controllar;

import com.ListOnGo.ServerListOnGo.Model.AllListModel;
import com.ListOnGo.ServerListOnGo.Model.GroupedListDTO;
import com.ListOnGo.ServerListOnGo.Model.UserModel;
import com.ListOnGo.ServerListOnGo.Repository.AllListRepository;
import com.ListOnGo.ServerListOnGo.Repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/list-on-go")
@CrossOrigin("*")
public class AllListController {
    @Autowired
    private AllListRepository listRepo;

    @Autowired
    private UserModelRepository userRepo;

    @PostMapping("/list/create-list")
    public ResponseEntity<?> createList(@RequestBody List<AllListModel> modelList, @RequestParam("userId") Long userId) {
        Optional<UserModel> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        UserModel user = userOptional.get();

        // Assign user to each item
        modelList.forEach(item -> item.setUserOfList(user));
        listRepo.saveAll(modelList);

        return ResponseEntity.ok("Done");
    }


    @GetMapping("/list/grouped-by-time")
    public ResponseEntity<?> getGroupedLists(@RequestParam Long userId) {
        List<AllListModel> allItems = listRepo.findByUserOfList_Id(userId);

        // Group by time (ignoring seconds and nanos)
        Map<LocalDateTime, List<AllListModel>> grouped = allItems.stream()
                .collect(Collectors.groupingBy(item ->
                        item.getDateAndTime().withSecond(0).withNano(0)
                ));

        // Convert to DTOs
        List<GroupedListDTO> response = grouped.entrySet().stream()
                .map(e -> {
                    String listName = e.getValue().get(0).getListName(); // Get list name from first item
                    return new GroupedListDTO(e.getKey(), e.getValue(), listName);
                })
                .sorted((a, b) -> b.getDateTime().compareTo(a.getDateTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("list/clear-list")
    public ResponseEntity<?> clearList(@RequestParam("userId") Long userId){
        listRepo.deleteListByUserId(userId);
        return  new ResponseEntity<>("Done",HttpStatus.OK);
    }


}