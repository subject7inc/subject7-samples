#*Reporting* application
 
##Setup
1. If you enabled Google API OAuth and downloaded credentials.json file just ignore next steps
2. Go to https://developers.google.com/drive/api/v3/quickstart/java
3. Click on "Enable the Drive API" blue button
4. Login and follow Google instructions to activate API
5. In the end you will see popup with Client ID, Client Secret and button to download credentials.json file. Use it to get the file.

##Usage

- If you want to create new Google Sheet file under special path on Google Drive use options from next example:

-s7_username *subject7*

-s7_password *my_password*

-s7_url *https://demo.subject-7.com*

-executions *16766*

-google_credentials_file_path */Users/shad0w/Developer/credentials.json*

-google_new_sheet_path */test/my/path*

-google_new_sheet_title *my_sheet*


- If you want to append information to existed Google Sheet get file ID from URL, replace parameters *google_new_sheet_path* and *google_new_sheet_title* with *google_sheet_id*:

-s7_username *subject7*

-s7_password *my_password*

-s7_url *https://demo.subject-7.com*

-executions *16766*

-google_credentials_file_path */Users/shad0w/Developer/credentials.json*

-google_sheet_id *my_id*