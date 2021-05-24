Root directory for any supporting files required by the Vagrant virtual machine.

`provision_privileged.sh` : defines provisioning of the Vagrant virtual machine in privileged mode 
(sudo). This script is executed only once, at the time the Vagrant virtual machine is first created.

`provision_user.sh` : defines provisioning of the Vagrant virtual machine in user mode (as 
'vagrant' user). This script is executed only once, at the time the Vagrant virtual machine is 
first created.

`docker/daemon.json` : provides DNS overrides to allow Docker to execute correctly within the 
Vagrant virtual machine.
