SRC_URI_append_kirkwood = "\
    file://init.mtd \
    file://ubinize.cfg \
"

RDEPENDS_${PN}_append_kirkwood = "\
    mtd-utils \
    mtd-utils-ubifs \
    mtd-utils-misc \
"

