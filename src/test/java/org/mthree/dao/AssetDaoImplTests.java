package org.mthree.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mthree.dao.mappers.AssetMapper;
import org.mthree.dto.Asset;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AssetDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AssetDaoImpl assetDao;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Create New Asset Test")
    public void createNewAssetTest() {
        Asset asset = new Asset();
        asset.setUserId(1);
        asset.setValue(BigDecimal.valueOf(1000.50));
        asset.setDescription("Test Asset");

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("id", 1));
            return 1;
        });

        Asset result = assetDao.createNewAsset(asset);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    @DisplayName("Get All Assets Test")
    public void getAllAssetsTest() {
        Asset asset = new Asset();
        asset.setId(1);
        asset.setUserId(1);
        asset.setValue(BigDecimal.valueOf(1000.50));
        asset.setDescription("Test Asset");
        List<Asset> assets = Arrays.asList(asset);

        when(jdbcTemplate.query(eq("SELECT * FROM assets_table"), any(AssetMapper.class))).thenReturn(assets);

        List<Asset> result = assetDao.getAllAssets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Asset", result.get(0).getDescription());
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM assets_table"), any(AssetMapper.class));
    }

    @Test
    @DisplayName("Find Asset By ID Test")
    public void findAssetByIdTest() {
        Asset asset = new Asset();
        asset.setId(1);
        asset.setUserId(1);
        asset.setValue(BigDecimal.valueOf(500.75));
        asset.setDescription("Find Me");

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM assets_table WHERE id = ?"),
                any(AssetMapper.class),
                eq(1)
        )).thenReturn(asset);

        Asset result = assetDao.findAssetById(1);

        assertNotNull(result);
        assertEquals("Find Me", result.getDescription());
        verify(jdbcTemplate, times(1)).queryForObject(
                eq("SELECT * FROM assets_table WHERE id = ?"),
                any(AssetMapper.class),
                eq(1)
        );
    }

    @Test
    @DisplayName("Update Asset Test")
    public void updateAssetTest() {
        Asset asset = new Asset();
        asset.setId(1);
        asset.setUserId(1);
        asset.setValue(BigDecimal.valueOf(200.00));
        asset.setDescription("Old Desc");

        // Stub the exact update call
        when(jdbcTemplate.update(
                eq("UPDATE assets_table SET user_id = ?, `value` = ?, description = ? WHERE id = ?"),
                eq(1),
                eq(BigDecimal.valueOf(200.00)),
                eq("New Desc"),
                eq(1)
        )).thenReturn(1);

        asset.setDescription("New Desc");
        assetDao.updateAsset(asset);

        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE assets_table SET user_id = ?, `value` = ?, description = ? WHERE id = ?"),
                eq(1),
                eq(BigDecimal.valueOf(200.00)),
                eq("New Desc"),
                eq(1)
        );
    }

    @Test
    @DisplayName("Delete Asset Test")
    public void deleteAssetTest() {
        when(jdbcTemplate.update(
                eq("DELETE FROM assets_table WHERE id = ?"),
                eq(1)
        )).thenReturn(1);

        assetDao.deleteAsset(1);

        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM assets_table WHERE id = ?"),
                eq(1)
        );
    }
}