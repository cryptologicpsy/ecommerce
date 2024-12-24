// Περιέχει τους έγκυρους ταχυδρομικούς κώδικες για την Ελλάδα
const validZipCodes = [
  "10001", "10002", "10003", "10004", "10005", // Ακολουθούν οι υπόλοιποι ταχυδρομικοί κώδικες
  "10558", "10559", "10560"
];

document.getElementById('checkout-form').addEventListener('submit', function(event) {
  event.preventDefault(); // Αποτρέπει την υποβολή της φόρμας για τον έλεγχο του ZIP code
  const zipInput = document.getElementById('zip');
  const zipError = document.getElementById('zip-error');
  const zipCode = zipInput.value.trim();

  if (!validZipCodes.includes(zipCode)) {
    zipError.style.display = 'block';
    zipInput.classList.add('is-invalid');
  } else {
    zipError.style.display = 'none';
    zipInput.classList.remove('is-invalid');
    // Υποβάλλει τη φόρμα αν ο ταχυδρομικός κώδικας είναι έγκυρος
    this.submit();
  }
});

document.getElementById('zip').addEventListener('input', function() {
  const zipError = document.getElementById('zip-error');
  zipError.style.display = 'none';
  this.classList.remove('is-invalid');
});
