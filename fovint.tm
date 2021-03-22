KPL/MK

  This is the meta-kernel used in the solution of the
  ``Intersecting Vectors with a Triaxial Ellipsoid'' task
  in the Remote Sensing Hands On Lesson.

  The names and contents of the kernels referenced by this
  meta-kernel are as follows:

  File name                   Contents
  --------------------------  -----------------------------
  naif0008.tls                Generic LSK
  cas00084.tsc                Cassini SCLK
  981005_PLTEPH-DE405S.bsp    Solar System Ephemeris
  020514_SE_SAT105.bsp        Saturnian Satellite Ephemeris
  030201AP_SK_SM546_T45.bsp   Cassini Spacecraft SPK
  cas_v37.tf                  Cassini FK
  04135_04171pc_psiv2.bc      Cassini Spacecraft CK
  cpck05Mar2004.tpc           Cassini Project PCK
  cas_iss_v09.ti              ISS Instrument Kernel
  phoebe_64q.bds              Phoebe DSK


  \begindata
  KERNELS_TO_LOAD = ( 'kernels2/lsk/naif0008.tls',
                      'kernels2/sclk/cas00084.tsc',
                      'kernels2/spk/981005_PLTEPH-DE405S.bsp',
                      'kernels2/spk/020514_SE_SAT105.bsp',
                      'kernels2/spk/030201AP_SK_SM546_T45.bsp',
                      'kernels2/fk/cas_v37.tf',
                      'kernels2/ck/04135_04171pc_psiv2.bc',
                      'kernels2/pck/cpck05Mar2004.tpc',
                      'kernels2/ik/cas_iss_v09.ti'
                      'kernels2/dsk/phoebe_64q.bds' )
  \begintext

