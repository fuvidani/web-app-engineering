package at.ac.tuwien.waecm.ss18.group09.service

open class ServiceException(msg: String) : Exception(msg)

class DuplicatedEmailException(msg: String) : ServiceException(msg)

class ValidationException(msg: String) : ServiceException(msg)