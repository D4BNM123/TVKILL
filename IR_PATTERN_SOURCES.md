# Additional IR pattern sources

The model-specific candidate patterns added in this release are generated from decoded protocol/address/command records in the public Flipper-IRDB collection. They are candidates, not universal manufacturer codes; TV models and remote revisions can differ.

- Samsung: https://github.com/UberGuidoZ/Flipper-IRDB/blob/main/TVs/Samsung/Samsung_TV_Full.ir
- LG: https://github.com/UberGuidoZ/Flipper-IRDB/blob/main/TVs/LG/LG_55UN7300AUD.ir
- Sony: https://github.com/UberGuidoZ/Flipper-IRDB/blob/main/TVs/Sony/Sony_Bravia.ir
- Sharp: https://github.com/UberGuidoZ/Flipper-IRDB/blob/main/TVs/Sharp/Sharp_Aquos_32BG3E.ir
- Toshiba: https://github.com/UberGuidoZ/Flipper-IRDB/blob/main/TVs/Toshiba/Toshiba_32AV502U.ir
- Toshiba: https://github.com/UberGuidoZ/Flipper-IRDB/blob/main/TVs/Toshiba/Toshiba_43LF421U21.ir
- Hisense: https://github.com/UberGuidoZ/Flipper-IRDB/blob/main/TVs/Hisense/Hisense_55E7KQ.ir
- TCL: https://github.com/UberGuidoZ/Flipper-IRDB/tree/main/TVs/TCL

Protocol timing definitions are based on the Flipper Zero infrared encoder documentation and source:
- https://developer.flipper.net/flipperzero/doxygen/infrared_file_format.html
- https://github.com/flipperdevices/flipperzero-firmware/tree/dev/lib/infrared/encoder_decoder

No codes are added for Xiaomi, Skyworth, or Coocaa in this change because the inspected Xiaomi file did not contain Power/Mute and the inspected Flipper-IRDB tree did not contain Skyworth or Coocaa TV entries. The existing Xiaomi raw file contains other navigation/volume signals, but no Power or Mute command to safely map into this app.

All added codes should be treated as model-specific candidates and tested with a compatible TV and IR emitter.
