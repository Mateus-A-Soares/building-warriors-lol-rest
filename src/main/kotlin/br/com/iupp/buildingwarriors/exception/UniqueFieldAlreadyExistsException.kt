package br.com.iupp.buildingwarriors.exception

class UniqueFieldAlreadyExistsException(val entity : String, val field : String) : Throwable()
