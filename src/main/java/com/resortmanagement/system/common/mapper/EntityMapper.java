/*
EntityMapper.java
Purpose:
 - Central mapping interface (MapStruct) or central manual mapper. Prefer small per-domain mappers (ReservationMapper, InvoiceMapper).
Recommended content:
 - If MapStruct used: @Mapper(componentModel = "spring") and mapping methods for entity <-> DTO pairs.
 - Keep logic-free mapping; any business transformation should be in service or specialized util.

File: common/mapper/EntityMapper.java
*/

package com.resortmanagement.system.common.mapper;

public interface EntityMapper<D, E> {
    D toDto(E entity);

    E toEntity(D dto);
}