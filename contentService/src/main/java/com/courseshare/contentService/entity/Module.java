package com.courseshare.contentService.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;





@AllArgsConstructor
@Data
public class Module {
   @Id
   private String id;
   private String name;
   private List<Leacture> lectures;

   public Module(){
      id = new ObjectId().toString();
   }
}
