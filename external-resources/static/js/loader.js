window.addEventListener('load', function() {
  var video = document.querySelector('video');
  var overlay = document.querySelector('.overlay');
  var logo = document.querySelector('.logo');

  // Σημαία για να ξέρουμε αν η σελίδα έχει φορτώσει
  var pageLoaded = false;
  var videoPlaying = false; // Αν δεν υπάρχει βίντεο, θεωρούμε το βίντεο έτοιμο

  // Έλεγχος αν υπάρχει ήδη το localStorage για να δούμε αν έχει φορτωθεί πριν η σελίδα
  var isFirstVisit = !localStorage.getItem('videoPlayed');

  // Όταν η σελίδα φορτώσει
  pageLoaded = true;

  if (isFirstVisit) {
    // Αν είναι η πρώτη επίσκεψη, αφαιρούμε άμεσα τον loader
    localStorage.setItem('videoPlayed', 'true'); // Αποθήκευση ότι το βίντεο έχει παιχτεί
    videoPlaying = true; // Αμέσως θεωρούμε ότι το βίντεο έχει παιχτεί
    checkAndRemoveLoader();
  }

  if (video) {
    // Όταν το βίντεο ξεκινήσει την αναπαραγωγή
    video.addEventListener('play', function() {
      setTimeout(function() {
        videoPlaying = true;
        checkAndRemoveLoader();
      }, 1000); // Προσθήκη επιπλέον 1 δευτερόλεπτου καθυστέρησης
    });

    // Ξεκινάμε την αναπαραγωγή του βίντεο όταν είναι έτοιμο
    video.addEventListener('canplaythrough', function() {
      video.play();
    });
  } else {
    // Αν δεν υπάρχει βίντεο, αφαιρούμε τον loader αμέσως
    videoPlaying = true;
    checkAndRemoveLoader();
  }

  // Συνάρτηση για να ελέγξει και να αφαιρέσει τον loader
  function checkAndRemoveLoader() {
    if (pageLoaded && videoPlaying) {
      // Αφαίρεση του loader
      document.body.classList.add('loaded'); // Προσθήκη κλάσης loaded
      if (overlay) overlay.style.display = 'none'; // Απόκρυψη του overlay

      if (logo) {
        logo.style.opacity = '0'; // Fade-out στο λογότυπο
        setTimeout(function() {
          logo.style.display = 'none'; // Αφαίρεση λογότυπου
        }, 500); // Επιτρέπουμε λίγο χρόνο για το fade-out
      }
    }
  }

  // Διασφαλίζουμε ότι το `checkAndRemoveLoader` καλείται αν η σελίδα φορτωθεί πλήρως
  window.addEventListener('DOMContentLoaded', function() {
    if (!isFirstVisit) {
      checkAndRemoveLoader();
    }
  });
});
