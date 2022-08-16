package com.owusu.cryptosignalalert.data.mappers

interface DataMapper<ObjA, ObjB> {
    fun transform(objA: ObjA): ObjB
    fun reverseTransformation(objB: ObjB): ObjA
}