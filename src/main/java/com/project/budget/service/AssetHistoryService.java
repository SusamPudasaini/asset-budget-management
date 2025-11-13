package com.project.budget.service;

import org.springframework.stereotype.Service;
import com.project.budget.entity.AssetHistoryEntity;
import com.project.budget.repository.AssetHistoryRepository;

import java.util.List;

@Service
public class AssetHistoryService {

    private final AssetHistoryRepository assetHistoryRepository;

    public AssetHistoryService(AssetHistoryRepository assetHistoryRepository) {
        this.assetHistoryRepository = assetHistoryRepository;
    }

    // Call the correct repository method
    public List<AssetHistoryEntity> getByBranchCode(String branchCode) {
        return assetHistoryRepository.findByBranch_BranchCode(branchCode);
    }
}
