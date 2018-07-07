## Setting up Virtual Machine Environment

**1.** Depending upon the OS choose the virtualization software, we chose Oracle Virtual Box. 
            https://www.virtualbox.org/

**2.** Create new virtual machine of type Linux 2.6, Name the Virtual Machine

**3.** Allocate Virtual Machine Storage as Virtual Drive or Partition and set the capacity to 16GB.

**4.** Choose the ISO Image android-4.4.4rc5.iso for initial boot.

**5.** Create Partition - No GPT and set properties as Primary; Bootable; Write

**6.** Install GRUB Loader; Skip EFI Grub Loader

**7.** Reboot Androidx86; Eject ISO

**8.** Download DroidVNC_x86.bin file and store it in root/system/bin folder of Android x86 OS.

**9.** Using root privileges make the droidvnc_x86.bin file executable.

**10.** Test execution of executable of DroidVNC_x86, ensure that it is running and note the port number and IP address.

**11.** Install VNCservice.apk.

**12.** Install and configure HyperdroidService.apk.

**13.** Run VNCservice with superuser privileges.

**14.** Run Hyperdroid services with superuser privileges.

**15.** Determine and monitor VM’s activity on firebase console.

**16.** Install AutoStart.apk and run it in superuser mode.

**17.** Add VNCservice and Hyperdroid service to be executed on startup.

```NOTE:``` Run VNC Service App first then HyperdroidService because they are dependent on each other.
