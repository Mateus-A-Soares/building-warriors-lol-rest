package br.com.iupp.buildingwarriors.exception

class FieldConstraintException(val entity: String, val fieldErrors: List<Pair<String, String>>) : RuntimeException()