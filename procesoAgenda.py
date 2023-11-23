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

import datetime as dt  # Usar 'dt' como alias en lugar de 'datetime'


import funcionesTreatment
from unidecode import unidecode

def accesAgenda ():
    # =============================================================================
    # Declaramos las rutas necesarias
    PATH = os.getcwd()
    OUTPUTDIR = PATH + "/../Output/"
    
	# Miramos si existe la carpeta, si no es el caso la creamos
    hoy = dt.date.today()
    CARPETAOUTPUT = OUTPUTDIR + hoy.strftime("%Y%m%d")
    if not os.path.exists(CARPETAOUTPUT):
        os.makedirs(CARPETAOUTPUT)

	# Descargo la agenda de Barcelona
    agenda = funcionesTreatment.accesAgenda()
    agenda = agenda[['register_id', 'prefix', 'suffix', 'name', 
					 'geo_epgs_25831', 'geo_epgs_23031', 'geo_epgs_4326', 
					 'sections_data', 'start_date', 'end_date', 'place', 
					 'district_name', 'district_id', 'neighborhood_name', 
					 'neighborhood_id', 'address_name', 'street_number_1']]
    agenda.to_csv(CARPETAOUTPUT + "/agendaBarcelona.csv",index=False, sep=';')

if __name__ == "__main__":
    accesAgenda()
    
