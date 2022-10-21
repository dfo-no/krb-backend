package org.kravbank.utils.product

interface ProductMapper<D,E> {

    fun getOneFromEntity (entity: E): D
    fun getListFromEntity (entity: E): D
    fun getRefFromEntity (entity: E): D
    fun postToEntity(domain: D): E
    fun putToEntity(domain: D): E

}