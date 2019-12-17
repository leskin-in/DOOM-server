# D592
A simple multiplayer online platformer.


## Download
Currently, D592 is available as a source distribution (this repository) or as a `.deb` file for Debian-based Linux systems (e.g. Ubuntu, Mint, Alpine).

The _latest_ versions of the `.deb` packets are available by the following links:
* [client](https://github.com/leskin-in/simpleplat/raw/master/deb-client/d592-client_0.4_all.deb)
* [server](https://github.com/leskin-in/simpleplat/raw/master/deb-server/d592-server_0.4_all.deb)


## Install
### Client
After downloading, open the console, go to the download directory and run
```
sudo dpkg -i ./d592-client_0.4_all.deb
```

In case there is no JRE or Python3 installed on the machine, install them by running the following command from the terminal:
```
sudo apt install -y openjdk-8-jre python3
```
After that, install the D592 client (using the command mentioned above).

### Server
Do the same as for the client. The command to run is
```
sudo dpkg -i ./d592-server_0.4_all.deb
```


## Run
To run the client, open the console and run
```
d592-client
```
A GUI with basic instructions appears.

To run the server, open the console and run
```
d592-server --help
```
This will display help. You can change run options (command line parameters) without stopping the server.

When the server is running, type `help` to get help. Press Return to get server state updates.
