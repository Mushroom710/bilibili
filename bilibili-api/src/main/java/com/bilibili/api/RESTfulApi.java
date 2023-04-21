package com.bilibili.api;

// @date 2023/4/21
// @time 20:55
// @author zhangzhi
// @description

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class RESTfulApi {

    private final Map<Integer, Map<String, Object>> dataMap;

    public RESTfulApi(){
        this.dataMap = new HashMap<>();
        for(int i = 0; i < 3; i++){
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", i);
            data.put("name", "name" + i);
            this.dataMap.put(i, data);
        }
    }

    @GetMapping("/objects/{id}")
    public Map<String, Object> getData(@PathVariable Integer id){
        return this.dataMap.get(id);
    }

    @DeleteMapping("/objects/{id}")
    public String deleteData(@PathVariable Integer id){
        this.dataMap.remove(id);
        return "delete success";
    }

    @PostMapping("/objects")
    public String postData(@RequestBody Map<String, Object> data){
        Integer[] idArray = this.dataMap.keySet().toArray(new Integer[0]);
        Arrays.sort(idArray);
        int nextId = idArray[idArray.length - 1] + 1;
        this.dataMap.put(nextId, data);
        return "post success";
    }

    @PutMapping("/objects")
    public String putData(@RequestBody Map<String, Object> data){
        Integer id = Integer.valueOf(String.valueOf(data.get("id")));
        Map<String, Object> containedData = this.dataMap.get(id);
        if(containedData == null){
            Integer[] idArray = this.dataMap.keySet().toArray(new Integer[0]);
            Arrays.sort(idArray);
            int nextId = idArray[idArray.length - 1] + 1;
            this.dataMap.put(nextId, data);
        }else{
            this.dataMap.put(id, data);
        }
        return "put success";
    }

}
