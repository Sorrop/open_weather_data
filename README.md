# open_weather_data

## Configuration

It is needed to include on top level directory a `config.edn` file with the following contents

```clojure
{:database {:dbtype "postgresql"
            :dbname "open_weather"
            :host "localhost"
            :user <database-user-name>
            :password <password>}
 :open-weather-api {:api-key <app-key>
                    :host "http://api.openweathermap.org/data/2.5/"}
 ;; data needed to validate city input
 :resources {:cities-ids-file "current.city.list.json"
             :country-codes-file "all_316_country_codes.json"
             ;; included in repo, can be created by appropriate task
             :city-matchings-file "city_matchings.edn"}
 ;; location to store csv files for sending/receiving to/from s3 bucket
 :csv-files-dir <full_path_to_csv_files_dir>
 ;; aws credentials and bucket name
 :aws-config {:creds {:access-key <access_key>
                      :secret-key <secret_key>
                      :endpoint   <endpoint>}
              :bucket <bucket_name>}}
```

## Development

Development is done with Emacs + CIDER. 

When you're on a clj buffer <kbd>M-x cider-jack-in-clj</kbd> or <kbd>M-x cider-jack-in</kbd>

Hit `(go)` when prompted on the clojure repl to start the system.

Hit `(reset)` when you have completed changes to reload changed files and restart the system.

## Usage

FIXME: explanation

    $ java -jar open_weather_data-0.1.0-standalone.jar [args]


## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
