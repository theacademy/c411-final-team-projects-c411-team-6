package org.mthree.dao;


import org.mthree.dao.mappers.AssetMapper;
import org.mthree.dto.Asset;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class AssetDaoImpl implements AssetDao{

    private final JdbcTemplate jdbc;

    public AssetDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Asset createNewAsset(Asset asset) {
        final String INSERT_ASSET = "INSERT INTO assets(user_id, value, description) VALUES(?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_ASSET, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, asset.getUserId());
            statement.setBigDecimal(2, asset.getValue());
            statement.setString(3, asset.getDescription());
            return statement;
        }, keyHolder);

        asset.setId(keyHolder.getKey().intValue());

        return asset;
    }

    @Override
    public List<Asset> getAllAssets() {
        final String SELECT_ALL_ASSETS = "SELECT * FROM assets";
        return jdbc.query(SELECT_ALL_ASSETS, new AssetMapper());
    }

    @Override
    public Asset findAssetById(int id) {
        try {
            final String SELECT_ASSET_BY_ID = "SELECT * FROM assets WHERE id = ?";
            return jdbc.queryForObject(SELECT_ASSET_BY_ID, new AssetMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateAsset(Asset asset) {

        final String UPDATE_ASSET = "UPDATE assets SET user_id = ?, value = ?, description = ? WHERE id = ?";

        jdbc.update(UPDATE_ASSET,
                asset.getUserId(),
                asset.getValue(),
                asset.getDescription(),
                asset.getId());

    }

    @Override
    public void deleteAsset(int id) {

        final String DELETE_ASSET = "DELETE FROM assets WHERE id = ?";
        jdbc.update(DELETE_ASSET, id);
    }
}
