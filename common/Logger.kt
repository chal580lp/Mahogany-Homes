package com.recursive.mahoganyhomes.common

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


fun getLogger(name: String): Logger = LogManager.getLogger(name)

