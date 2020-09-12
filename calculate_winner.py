import matplotlib.pyplot as plt
import pandas as pd
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.models import model_from_json
from keras.layers.core import Dense, Dropout
from keras.layers import BatchNormalization
from keras.optimizers import Adam
from keras.utils import to_categorical
from keras.callbacks import ReduceLROnPlateau,EarlyStopping,ModelCheckpoint
from sklearn.metrics import accuracy_score
import numpy as np
import time
from joblib import dump,load

def load_trained_ann():
    try:
        # Ucitaj JSON i kreiraj arhitekturu neuronske mreze na osnovu njega
        json_file = open('Serialization_folder/neuronska.json', 'r')
        loaded_model_json = json_file.read()
        json_file.close()
        ann = model_from_json(loaded_model_json)
        # ucitaj tezine u prethodno kreirani model
        ann.load_weights("Serialization_folder/neuronska.h5")
        #print("Neuronska uspesno ucitana.")
        return ann
    except Exception as e:
        # ako ucitavanje nije uspelo, verovatno model prethodno nije serijalizovan pa nema odakle da bude ucitan
        return None


contestant_1 = input("Please enter contestant_1 name:\n")
contestant_2 = input("Please enter contestant_2 name:\n")
court = input("Please enter court type (Indoor/Outdoor):\n")
location = input("Please enter location name :\n")
series = input("Please enter series name: (Grand Slam)\n")
surface = input("Please enter surface type: (Hard)\n")
bestof = input("Please enter game duration (Best of 3/5) type:\n")
tournament = input("Please enter tournament name: (Australian Open)\n")
input = []


contestant_1_mapping = load("Serialization_folder/contestant_1_mapping.joblib")
contestant_2_mapping = load("Serialization_folder/contestant_2_mapping.joblib")
court_mapping = load("Serialization_folder/court_mapping.joblib")
location_mapping = load("Serialization_folder/location_mapping.joblib")
series_mapping = load("Serialization_folder/series_mapping.joblib")
surface_mapping = load("Serialization_folder/surface_mapping.joblib")
tournament_mapping = load("Serialization_folder/tournament_mapping.joblib")


for key,value in location_mapping.items():
    if value == location:
        location = key

for key,value in tournament_mapping.items():
    if value == tournament:
        tournament = key

for key,value in series_mapping.items():
    if value == series:
        series = key

for key,value in court_mapping.items():
    if value == court:
        court = key

for key,value in surface_mapping.items():
    if value == surface:
        surface = key

for key,value in contestant_1_mapping.items():
    if value == contestant_1:
        contestant_1 = key

for key,value in contestant_2_mapping.items():
    if value == contestant_2:
        contestant_2 = key

input_list = []
input.append(location)
input.append(tournament)
input.append(series)
input.append(court)
input.append(surface)
input.append(bestof)
input.append(contestant_1)
input.append(contestant_2)

input_list = np.array([input])
dump(input,"input.joblib")

model = load_trained_ann()
res = model.predict(input_list)
print(winner_mapping[res])
print(res)