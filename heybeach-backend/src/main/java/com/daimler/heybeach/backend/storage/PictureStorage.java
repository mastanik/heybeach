package com.daimler.heybeach.backend.storage;

import com.daimler.heybeach.backend.exception.PictureStorageException;

public interface PictureStorage {
    public String store(String filename, byte[] data) throws PictureStorageException;

    public byte[] retrieve(String path) throws PictureStorageException;
}
