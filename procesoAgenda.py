# -*- coding: utf-8 -*-
"""
Created on Tue Oct 24 00:54:25 2023

@author: User
"""
import pandas as pd
import numpy as np
import requests
import re
import os
import datetime as dt


import funcionesTreatment
from unidecode import unidecode

def accesAgenda ():
    # =============================================================================
    # Declaramos las rutas necesarias
    PATH = os.getcwd()
    OUTPUTDIR = PATH + "/../Output/"
    
	# Miramos si existe la carpeta, si no es el caso la creamos
    CARPETAOUTPUT = OUTPUTDIR + dt.date.today().strftime("%Y%m%d")
    if not os.path.exists(CARPETAOUTPUT):
        os.makedirs(CARPETAOUTPUT)

	# Descargo la agenda de Barcelona
    agenda = funcionesTreatment.accesAgenda()
    agenda.to_csv(CARPETAOUTPUT + "agendaBarcelona.csv",index=False)

if __name__ == "__main__":
    accesAgenda()
    
