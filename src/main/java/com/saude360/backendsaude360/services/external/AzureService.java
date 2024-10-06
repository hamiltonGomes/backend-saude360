package com.saude360.backendsaude360.services.external;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class AzureService {

    @Value("${spring.cloud.azure.storage.blob.account-name}")
    private String accountName;

    @Value("${spring.cloud.azure.storage.blob.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Value("${spring.cloud.azure.storage.blob.account-key}")
    private String accountKey;

    private BlobContainerClient getBlobContainerClient() {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
        return new BlobContainerClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .containerName(containerName)
                .buildClient();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient();
        String fileName = UUID.randomUUID() + file.getOriginalFilename();
        containerClient.getBlobClient(fileName).upload(file.getInputStream(), file.getSize(), true);
        return fileName;
    }

    public String downloadFile(String fileName) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient();
        InputStream inputStream = containerClient.getBlobClient(fileName).openInputStream();
        byte[] fileBytes = IOUtils.toByteArray(inputStream);
        return Base64.getEncoder().encodeToString(fileBytes);
    }
}
