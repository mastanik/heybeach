package com.daimler.heybeach.backend.storage;

import com.daimler.heybeach.backend.exception.PictureStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesystemStorage implements PictureStorage {

    private String path;

    public FilesystemStorage(String path) {
        this.path = path;
    }

    @Override
    public String store(String filename, byte[] data) throws PictureStorageException {
        try {
            Path fspath = Paths.get(path + "/" + filename);
            while (Files.exists(fspath)) {
                Integer suffix = 1;
                filename += suffix;
                fspath = Paths.get(path + "/" + filename);
            }
            return Files.write(fspath, data).toString();
        } catch (IOException e) {
            throw new PictureStorageException("Exception occurred while trying to save picture", e);
        }
    }

    @Override
    public byte[] retrieve(String path) throws PictureStorageException {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new PictureStorageException("Exception occurred while trying to fetch picture", e);
        }
    }
}
