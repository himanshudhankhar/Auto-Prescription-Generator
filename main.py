import os
#import magic
import urllib.request
from app import app
from flask import Flask, flash, request, redirect, render_template
from werkzeug.utils import secure_filename

ALLOWED_EXTENSIONS = set(['3gpp', 'mpeg', 'mpeg_2_ts', 'wav'])

def allowed_file(filename):
	return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/patient_audio', methods=['POST'])
def upload_file():
	if request.method == 'POST':
        # check if the post request has the file part
		if 'file' not in request.files:
			flash('No file part')
			return "No file part"
		file = request.files['file']
		if file.filename == '':
			flash('No file selected for uploading')
			return "No file selected for uploading"
		if file and allowed_file(file.filename):
			filename = secure_filename(file.filename)
			file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
			flash('File successfully uploaded')
			return  "File successfully uploaded"
		else:
			flash('Allowed file types are 3gpp ')
			return "3gpp file not found"

if __name__ == "__main__":
    app.run(port=5000)