# TPA
TPA is a simple plugin giving the ability to request teleports to others and go back to your death location if you die.

### Custom Translations
All messages will be translated to the clients set language if a translation for that language is present, otherwise it'll use the first entry from the configs translations as fallback (default: english)

Translation files are saved as .properties files in the translations folder.

###### Editing an already existing translation:
1. Edit and save the corresponding .properties file
2. Add the language code in the config.yml under translations if not already added
3. Either restart the server or reload the translations (`/tpa reload-translations`) and (if you changed something in the config too) reload the configs (`/tpa reload-configs`)

###### Adding new translations:
1. Create a new file `general_<language-code>.properties` in the translations folder
2. at best copy the contents from `general_en.properties` and change them according to your favored language
3. Add the new language to the config.yml under translations
4. Either restart the server or reload the translations (`/tpa reload-translations`) and reload the configs (`/tpa reload-configs`)
5. You're welcome to [create a pull request](../../pulls) and add your translation so others don't have to create their own

### Permissions
- `/back` - `tpa.back`
- `/tpaccept <player>` - `tpa.tpaccept`
- `/tpa <player>` - `tpa.tpa`
- `/tpa reload-config` - `tpa.reload-config`
- `/tpa reload-translations` - `tpa.reload-translations`
- `/tpahereall` - `tpa.tpahereall`
- `/tpahere <player>` - `tpa.tpahere`
- `/tpcancel <player>` - `tpa.tpcancel`
- `/tpdeny <player>` - `tpa.tpdeny`
- `/tptoggle` - `tpa.tptoggle`

### License
This project is licensed under the [GNU GPLv3](LICENSE).

&copy; 2022 l4zs
