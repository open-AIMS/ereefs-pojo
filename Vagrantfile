# -*- mode: ruby -*-
# vi: set ft=ruby :

#
# Bento Ubuntu instance for "ereefs-pojo" project.
#
Vagrant.configure(2) do |config|

  config.vm.box = "bento/ubuntu-18.10"
  config.vm.hostname = "vagrant-ereefs-pojo"

  # Virtualbox-specific overrides.
  config.vm.provider "virtualbox" do |v|
    v.name = "vagrant-ereefs-pojo"
  end

  #
  # SyncFolders.
  #

  # None.


  #
  # Port forwarding.
  #

  # Provide access to services running in the VM.
  # NOTE: These are normally disabled. Host-based access to services, including MongoDB, should
  # normally be achieved via the 'ereefs-vm' project.
 config.vm.network :forwarded_port, guest: 27017, host: 27017, id: 'mongodb'


  #
  # Provisioning.
  #

  # Install the latest Docker using the built-in Vagrant Docker provisioner.
  config.vm.provision :docker

  # Provisioning script executed in privileged mode (sudo).
  config.vm.provision "shell", path: "./vagrant/provision_privileged.sh"

  # Provisioning script executed in user mode (as 'vagrant' user).
  config.vm.provision "shell", path: "./vagrant/provision_user.sh"

end
