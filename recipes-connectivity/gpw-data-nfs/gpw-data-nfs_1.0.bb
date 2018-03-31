# Copyright (C) 2015 Jens Rehsack <sno@netbsd.org>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Initscripts for Development Data"
HOMEPAGE = "http://act.yapc.eu/gpw2018/index.html"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += " update-rc.d-native"
RDEPENDS_${PN} += " nfs-utils"
PR = "r0"

SRC_URI = "file://mount-gpw-data.sh"

do_install () {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/mount-gpw-data.sh ${D}${sysconfdir}/init.d

    update-rc.d -r ${D} mount-gpw-data.sh start 13 3 5 .
}
