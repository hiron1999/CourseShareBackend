package com.example.AwsService.service;


import com.example.AwsService.constaints.AWSResourceConstant;
import com.example.AwsService.entity.AWSResourceEntity;
import com.example.AwsService.entity.DataStore;
import com.example.AwsService.entity.MappingResourceEntity;
import com.example.AwsService.reposetory.DataStoreRepo;
import com.example.AwsService.reposetory.MappingResourceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class DatabaseManagementService {
    private static final Logger log = LoggerFactory.getLogger(DatabaseManagementService.class);
    @Autowired
    private DataStoreRepo dataStoreRepo;

    @Autowired
    private MappingResourceRepo mappingResourceRepo;

    public DataStore addDataStore(DataStore dataStore){

       return dataStoreRepo.findById(dataStore.getStoreId()).orElse(dataStoreRepo.save(dataStore));
    }
    public DataStore getDatastore(String id) {
        return dataStoreRepo.findById(id).orElseThrow();
    }

    public MappingResourceEntity createResourceMapping(String courceResourceId , String dataStoreId,String resourceType){
            MappingResourceEntity mappingResource = new MappingResourceEntity();
            mappingResource.setCourceResourceId(courceResourceId);
            try {
                AWSResourceEntity awsResource = createResource(dataStoreId,resourceType);
                awsResource.setMappingResourceEntity(mappingResource);
                mappingResource.setAwsResourceEntity(awsResource);
                mappingResource = mappingResourceRepo.save(mappingResource);
            }catch (Exception e){
                throw new RuntimeException("Resource Mapping failed : "+e.getLocalizedMessage());
            }
            return mappingResource;
    }

    private AWSResourceEntity createResource(String dataStoreId,String type) {
        AWSResourceEntity resource = new AWSResourceEntity();
      try{

        DataStore dataStore =getDatastore(dataStoreId);
        resource.setDataStore(dataStore);
        resource.setType(type);
        resource.setStatus(AWSResourceConstant.RESOURCE_STATUS_PENDING);
        }catch (Exception e){
        throw new RuntimeException("Resource creation failed :"+ e.getLocalizedMessage());
        }
       return resource;
    }

    public void updateResourceStatus(long resourceId,String status){
        mappingResourceRepo.findById(resourceId)
                .map(mappingResourceEntity -> {
                    log.info("Updating resource status -->{}", status);
                    mappingResourceEntity.getAwsResourceEntity().setStatus(status);
                    return mappingResourceRepo.save(mappingResourceEntity);
                })
                .orElseThrow();
    };

    public AWSResourceEntity getAwsResourceByCourseResourceId(String courseResourceId){
        return mappingResourceRepo.findBycourceResourceId(courseResourceId)
                .map(MappingResourceEntity::getAwsResourceEntity)
                .orElse(null);
    }


}
