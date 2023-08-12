### OTP Validator Service



#### Single Hashing technique

<p>
For security reasons, many s/w applications don't store the raw OTP value, 
then the question comes into your mind, how does otp verification works without storing the OTP?

Here is the secure solution that is Hashing technique.

Instead of storing the plain OTP, the system has to store the hash value which is generated from the OTP.
To generate the hash-value, we can use one-way encryption algorithms, those are MD5, SHA-1, SHA-256 and SHA-512.

These encryption algorithms generates a unique cipher text for the given input, that will be in hexadecimal format,
to generate the same hash-value you've to give same input.

We can use these algorithms at the time of OTP generation, and you can return the plain OTP to end-user 
after storing the hash-value from it.

When you get an active OTP for verification process, you've to generate the hash-value for the given OTP, 
 and then compare it with stored hash-value.

If both the hash-values are same then you can consider the verification process is succeeded.
</p>

#### Double Hashing technique

