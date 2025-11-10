import streamlit as st
import re

# st.set_page_config(page_title="Registration Form", layout="centered")

# st.markdown("<h2 style='text-align: center;'>🎓 Student Registration</h2>", unsafe_allow_html=True)
st.markdown("🎓 Student Registration", unsafe_allow_html = True)

# Function to validate email
def is_valid_email(email):
    pattern = r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,}$"
    return bool(re.fullmatch(pattern, email))

# Input fields
name = st.text_input("Full Name")
email = st.text_input("Email")
department = st.text_input("Department")
year = st.number_input("Year", min_value=1, max_value=10, step=1)

# Submit Button
if st.button("Register"):
    if not name or not email or not department:
        st.error("⚠️ Please fill all fields.")
    elif not is_valid_email(email):
        st.error("❌ Invalid Email Format. Example: user@example.com")
    else:
        st.success(f"✅ Registered Successfully!\n\n**Name:** {name}\n**Department:** {department}\n**Year:** {int(year)}")
