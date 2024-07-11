package com.example.AwsService.util;

import Model.course.ContentType;
import com.example.AwsService.constaints.AWSResourceConstant;
import com.example.AwsService.entity.AWSResourceEntity;

import java.io.File;
import java.util.Objects;

public class AWSutil {

    public static String generateCorseDirectory(String id){
        if(id==null || id.isEmpty() ){
            throw new NullPointerException("Course ID can not be null");
        }
        return AWSResourceConstant.DATA_STORE_CONTENT_DIRECTORY_PREFIX+id;
    }

    public static String convertAwsResourceType(ContentType contentType ){
        String resourceType = null;
        switch (contentType) {
            case IMAGE -> {
                resourceType = AWSResourceConstant.RESOURCE_TYPE_IMAGE;
            }

            case VIDEO -> {
                resourceType = AWSResourceConstant.RESOURCE_TYPE_VIDEO;
            }

            case AUDIO -> {
                resourceType =AWSResourceConstant.RESOURCE_TYPE_AUDIO;
            }

            case TEXT -> {
                resourceType =AWSResourceConstant.RESOURCE_TYPE_TXT;
            }

            case OTHER_RESOURCES -> {
                resourceType =AWSResourceConstant.RESOURCE_TYPE_COMPRESSED;
            }
            default -> throw new RuntimeException("Rrsource Type is incorrect");

        }
        return resourceType;
    }
    public static boolean isAllowedFileType(String mmeType, String allowedType) {
        String delimiter = "/";
        String [] value = mmeType.split(delimiter);
        if(value.length==1) {
            return false;
        }
        return value[0].toLowerCase().equals(allowedType);
    }
    public static String getResourceLocation(AWSResourceEntity resource){
        if(resource!=null){
            String courseDir = resource.getDataStore().getStoreId();
            String subdir;
            if (Objects.equals(resource.getType(), "video")) {
                subdir = AWSResourceConstant.RESOURCE_LOCATION_VIDEOS;
            } else {
                subdir = AWSResourceConstant.RESOURCE_LOCATION_FILES;
            }
            return courseDir+"/"+subdir+"/"+resource.getResourceId();
        }else{
            throw new NullPointerException("Resourse is null");
        }
    }
}
