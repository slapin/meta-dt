DESCRIPTION = "Open Source release of Motif 2.3.4 library"
SRC_URI = "git://git.code.sf.net/p/motif/code"
S = "${WORKDIR}/git"
inherit autotools

DEPENDS = "flex-native bison-native xbitmaps virtual/libx11 libxt libxft xproto jpeg libpng"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"
SRCREV = "9f0120a4452380af91fe205434849b8a1ae884dc"

CACHED_CONFIGUREVARS += "ac_cv_file__usr_X_include_X11_X_h=no \
                         ac_cv_file__usr_X11R6_include_X11_X_h=no"

PARALLEL_MAKE = ""

EXTRA_OECONF += "--enable-png --enable-jpeg"

PR = "r1"

do_configure() {
    cd ${S}
    cp ${STAGING_DATADIR_NATIVE}/libtool/config/ltmain.sh ${S}/build
    rm -f ${S}/libtool
    aclocal -I.
    libtoolize --force --copy --automake
    gnu-configize
    autoconf -f
    autoheader
    automake --foreign  --include-deps --add-missing
    cd ${B}
    oe_runconf
    touch ${B}/config/util/makestrs
    (
        # HACK: build a native binaries need during the build
        unset CC LD CXX CCLD CFLAGS
        oe_runmake -C config/util LIBTOOL="${BUILD_SYS}-libtool" CC="${BUILD_CC}" LD="${BUILD_LD}" CXX="${BUILD_CXX}" LIBS="" makestrs
        oe_runmake -C tools/wml LIBTOOL="${BUILD_SYS}-libtool" AR="${BUILD_AR}" RANLIB="${BUILD_RANLIB}" CC="${BUILD_CC}" LD="${BUILD_LD}" CXX="${BUILD_CXX}" LIBS="" wmluiltok wml
    )
    if [ "$?" != "0" ]; then
        exit 1
    fi
}

do_compile() {
	oe_runmake -C lib
	oe_runmake -C include
}

do_install() {
	oe_runmake -C lib install DESTDIR=${D}
	oe_runmake -C include install DESTDIR=${D}
}

LEAD_SONAME = "libXm.so.4"

