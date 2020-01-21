# Copyright (C) 2015 Jens Rehsack <sno@netbsd.org>
# Released under the GPL-2.0 license (see COPYING.MIT for the terms)

DESCRIPTION = "U-Boot BootScripts for linux-wendy"
PACKAGES = "bootscript-wendy-${WANTED_ROOT_DEV}"
PN = "bootscript-wendy-${WANTED_ROOT_DEV}"
PROVIDES = "bootscript-wendy-${WANTED_ROOT_DEV}"

inherit bootscript

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PACKAGE_ARCH = "${MACHINE_ARCH}-${WANTED_ROOT_DEV}"

DEPENDS = "u-boot-mkimage-native"
RDEPENDS_${PN} = "u-boot"

FILESEXTRAPATHS_prepend := "${THISDIR}/bootscript-${MACHINE}-${PV}:"

SRC_URI = "file://bootscript.mmc \
           file://bootscript.usb \
           file://bootscript.nfs \
"

COMPATIBLE_MACHINE = "(wendy)"

do_install () {
    set -x
    console_dev="$(echo ${SERIAL_CONSOLE} | awk '{$2}')"
    test -z "${console_dev}" && console_dev="${KERNEL_CONSOLE}"
    console_baud="$(echo ${SERIAL_CONSOLE} | awk '{$1}')"
    test -z "${console_baud}" && console_baud="9600"
    DEFAULT_BOOTSCRIPT="bootscript.mmc"
    test -f ${WORKDIR}/bootscript.${WANTED_ROOT_DEV} && DEFAULT_BOOTSCRIPT="bootscript.${WANTED_ROOT_DEV}"
    kernel_devicetree_spc=""
    kernel_devicetree_tmp=""
    for kdt in ${KERNEL_DEVICETREE}
    do
        kernel_devicetree_tmp="${kernel_devicetree_tmp}${kernel_devicetree_spc}$(basename ${kdt})"
        kernel_devicetree_spc=" "
    done
    kernel_devicetree="${kernel_devicetree_tmp}"
    sed -i -e "s/@UBOOT_LOADADDRESS[@]/${UBOOT_LOADADDRESS}/g" -e "s/@UBOOT_FDTADDRESS[@]/${UBOOT_FDTADDRESS}/g" \
           -e "s/@KERNEL_IMAGETYPE[@]/${KERNEL_IMAGETYPE}/g" -e "s/@KERNEL_DEVICETREE[@]/${kernel_devicetree}/g" \
           -e "s/@ROOT_DEV_NAME[@]/${ROOT_DEV_NAME}/g" -e "s/@ROOT_DEV_SEP[@]/${ROOT_DEV_SEP}/g" \
           -e "s/@UBOOT_MMC_DEV[@]/${UBOOT_MMC_DEV}/g" \
           -e "s/@MACHINE[@]/${MACHINE}/g" -e "s/@BRANCH[@]/${METADATA_BRANCH}/g" \
         ${WORKDIR}/${DEFAULT_BOOTSCRIPT}
    install -d ${D}/boot
    uboot-mkimage -T script -C none -n 'Wendy Script' -d ${WORKDIR}/${DEFAULT_BOOTSCRIPT} ${D}/boot/bootscript.${WANTED_ROOT_DEV}
}

FILES_${PN} += "/boot/bootscript*"
