package net.orekyuu.javatter.api.service;

import com.gs.collections.api.list.ImmutableList;
import net.orekyuu.javatter.api.column.ColumnState;

/**
 * カラムの状態の保存/復元を行うServiceです。
 */
@Service
public interface ColumnStateStorageService {

    void save(ImmutableList<ColumnState> columnStates);

    ImmutableList<ColumnState> loadColumnStates();
}
