# Agentske Tehnologije Projekat

EAR aplikaciju je potrebno pokrenuti na wildfly 11 serveru. Inicijalni master čvor se pokreće sa adrese 192.168.56.1. Angular aplikacija se pokreće 
komandom $ng serve --host 0.0.0.0. EAR aplikacija radi na portu 8080 a Angular aplikacija na portu 4200. 
Aplikacija omogućava dodavanje i zaustavljanje agenata. Agenti mogu da međusobno razmenjuju poruke. Osnovni tipovi agenata koji su podržani od strane aplikacije
su Master, Predictor i Collector. Takođe postoji mogućnost da se dodaju Ping i Pong agenti radi demonstracije razmene poruka. Aplikacja podržava komunikaciju između
više računara. Odrađen je default zadatak.
