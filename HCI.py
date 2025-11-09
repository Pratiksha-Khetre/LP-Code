from tkinter import *

# Create the main window
root = Tk()
root.title("Student Registration Form")
root.geometry("400x400")  # Width x Height

# --- Create Labels and Entry Fields ---
Label(root, text="Student Registration", font=("Arial", 16, "bold")).grid(row=0, column=1, pady=10)

Label(root, text="Full Name:").grid(row=1, column=0, padx=10, pady=5, sticky=E)
name_entry = Entry(root, width=30)
name_entry.grid(row=1, column=1)

Label(root, text="Email:").grid(row=2, column=0, padx=10, pady=5, sticky=E)
email_entry = Entry(root, width=30)
email_entry.grid(row=2, column=1)

Label(root, text="Department:").grid(row=3, column=0, padx=10, pady=5, sticky=E)
dept_entry = Entry(root, width=30)
dept_entry.grid(row=3, column=1)

Label(root, text="Year:").grid(row=4, column=0, padx=10, pady=5, sticky=E)
year_entry = Entry(root, width=30)
year_entry.grid(row=4, column=1)

# --- Submit Button Function ---
def submit_form():
    name = name_entry.get()
    email = email_entry.get()
    dept = dept_entry.get()
    year = year_entry.get()
    
    result_label.config(text="Registered " + name + " (" + dept + ", Year " + year + ")", fg="green")

# --- Button ---
Button(root, text="Submit", command=submit_form, bg="blue", fg="white").grid(row=5, column=1, pady=10)

# --- Result Label ---
result_label = Label(root, text="")
result_label.grid(row=6, column=1)

# Run the main event loop
root.mainloop()
