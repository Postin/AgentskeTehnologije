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

def create_ann(n_inputs,n_outputs):
    '''
    Implementirati veštačku neuronsku mrežu sa 28x28 ulaznih neurona i jednim skrivenim slojem od 128 neurona.
    Odrediti broj izlaznih neurona. Aktivaciona funkcija je sigmoid.
    '''
    ann = Sequential()
    # Postaviti slojeve neurona mreže 'ann'
    ann.add(Dense(512, input_dim=n_inputs, activation='relu'))
    ann.add(BatchNormalization(axis=-1))
    ann.add(Dropout(0.25))
    ann.add(Dense(512, activation='relu'))
    ann.add(BatchNormalization(axis=-1))
    ann.add(Dropout(0.25))
    ann.add(Dense(512, activation='relu'))
    ann.add(BatchNormalization(axis=-1))
    ann.add(Dropout(0.25))
    ann.add(Dense(512, activation='relu'))
    ann.add(BatchNormalization(axis=-1))
    ann.add(Dense(n_outputs, activation='softmax'))
    return ann


def train_ann(ann, X_train, y_train):
    X_train = np.array(X_train, np.float32)
    y_train = np.array(y_train, np.float32)


    # definisanje parametra algoritma za obucavanje
    adam = Adam(lr=0.001, beta_1=0.9, beta_2=0.999, amsgrad=False)
    ann.compile(loss='mean_squared_error', optimizer=adam, metrics=["accuracy"])

    learning_rate_reduction = ReduceLROnPlateau(monitor='accuracy',
                                                factor=0.5,
                                                patience=7,
                                                min_lr=0.0001,
                                                verbose=1)
    filepath="weights.best.hdf5"
    early_stopping = EarlyStopping(monitor='loss', patience=12, verbose=0, mode='min')
    mcp_save = ModelCheckpoint(filepath, save_best_only=True, monitor='loss', mode='min')

    # obucavanje neuronske mreze
    ann.fit(X_train, y_train, epochs=500, batch_size=100, verbose=2,
            shuffle=False, callbacks=[early_stopping, mcp_save, learning_rate_reduction])
    ann.load_weights("weights.best.hdf5")
    return ann

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


def serialize_ann(ann):
    # serijalizuj arhitekturu neuronske mreze u JSON fajl
    model_json = ann.to_json()
    with open("Serialization_folder/neuronska.json", "w") as json_file:
        json_file.write(model_json)
    # serijalizuj tezine u HDF5 fajl
    ann.save_weights("Serialization_folder/neuronska.h5")


def get_key(val,my_dict):
    for key, value in my_dict.items():
        if value == val:
            return key

    return "key doesn't exist"

excel_df = pd.read_excel('Data/2012.xls')
#df = excel_df.to_csv(index=False)

#print(excel_df.isnull().sum().max()) # neam null vrednosti

#corr = excel_df.corr()
#corr.style.background_gradient(cmap='coolwarm', axis=None)
#plt.matshow(corr)


X = excel_df.drop('Winner',axis=1)
#X = pd.get_dummies(X)
y = excel_df['Winner']
#One hot encoding 'Winner' column
# mapping = {}
# for i in range(len(y)):
#     mapping[y[i]] = i
#
# for i in range(len(y)):
#     y[i] = mapping[y[i]]
#
# y = to_categorical(y)

excel_df['Winner'] = excel_df['Winner'].astype('category')
winner_mapping = dict(enumerate(excel_df['Winner'].cat.categories))
excel_df['Winner'] = excel_df['Winner'].cat.codes
y = excel_df['Winner']
y = to_categorical(y)
#-------------------------------


#One hot encoding/label encoding 'Location' column
X['Location'] = X['Location'].astype('category')
location_mapping = dict(enumerate(X['Location'].cat.categories))
X['Location'] = X['Location'].cat.codes


#One hot encoding 'Tournament' column
X['Tournament'] = X['Tournament'].astype('category')
tournament_mapping = dict(enumerate(X['Tournament'].cat.categories))
X['Tournament'] = X['Tournament'].cat.codes


#One hot encoding 'Series' column
X['Series'] = X['Series'].astype('category')
series_mapping = dict(enumerate(X['Series'].cat.categories))
X['Series'] = X['Series'].cat.codes


#One hot encoding 'Court' column
X['Court'] = X['Court'].astype('category')
court_mapping = dict(enumerate(X['Court'].cat.categories))
X['Court'] = X['Court'].cat.codes


#One hot encoding 'Surface' column
X['Surface'] = X['Surface'].astype('category')
surface_mapping = dict(enumerate(X['Surface'].cat.categories))
X['Surface'] = X['Surface'].cat.codes


#One hot encoding 'Contestant_1' column
X['Contestant_1'] = X['Contestant_1'].astype('category')
contestant_1_mapping = dict(enumerate(X['Contestant_1'].cat.categories))
X['Contestant_1'] = X['Contestant_1'].cat.codes

#One hot encoding 'Contestant_2' column
X['Contestant_2'] = X['Contestant_2'].astype('category')
contestant_2_mapping = dict(enumerate(X['Contestant_2'].cat.categories))
X['Contestant_2'] = X['Contestant_2'].cat.codes

model = load_trained_ann()
if model is not None:
    dump(location_mapping,"location_mapping.joblib")
    dump(court_mapping,"court_mapping.joblib")
    dump(series_mapping,"series_mapping.joblib")
    dump(surface_mapping,"surface_mapping.joblib")
    dump(tournament_mapping,"tournament_mapping.joblib")
    dump(contestant_1_mapping,"contestant_1_mapping.joblib")
    dump(contestant_2_mapping,"contestant_2_mapping.joblib")
    dump(winner_mapping,"winner_mapping.joblib")


X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Turn the values into an array for feeding the classification algorithms.
X_train = X_train.values
X_test = X_test.values
#y_train = y_train.values
#y_test = y_test.values

t1 = time.time()

# neural network
n_inputs = X_train.shape[1]
print(n_inputs)
n_outputs = y_train.shape[1]

ann = create_ann(n_inputs,n_outputs)
ann = train_ann(ann,X_train,y_train)

t2 = time.time()
print("Treniranje modela zavrseno.")
print('model training time:', t2-t1)
serialize_ann(ann)

y_test_pred = ann.predict(X_test)
y_test_pred = np.argmax(y_test_pred,axis=1)

# uredjivanje y_test u listu da ne bude one hot i da se moze koristiti za accuracy score
test_list = []
for i in y_test:
    test_list.append(np.argmax(i))

print(y_test_pred)

print("Validation accuracy: ", accuracy_score(test_list, y_test_pred))
serialize_ann(ann)

