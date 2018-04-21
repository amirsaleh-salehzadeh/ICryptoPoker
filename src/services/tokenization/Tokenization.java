package services.tokenization;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import game.poker.holdem.domain.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class Tokenization {
	// The JWT signature algorithm we will be using to sign the token
	// //We will sign our JWT with our ApiKey secret
	private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
	private static byte[] apiKeySecretBytes;
	private static Key signingKey;

	public static String getPublicKey(String id, Player ent, Date expdate) {

		apiKeySecretBytes = DatatypeConverter.parseBase64Binary(ent.getPassword());
		signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder().setId(id).setSubject(ent.getName()).setIssuer("icryptopoker")
				.signWith(SignatureAlgorithm.HS512, signingKey);

		builder.setExpiration(expdate);

		return builder.compact();

	}

	public static boolean verifyPublic(String token, Player ent) {
		try {

			 Jwts.parser().requireSubject(ent.getName())
					.setSigningKey(DatatypeConverter.parseBase64Binary(ent.getPassword()))
					.parseClaimsJws(token).getBody();
		      return true ;
		} catch (SignatureException e) {
			System.out.println("cant trust token");
			return false;

		} catch (ExpiredJwtException e) {
			System.out.println("expired token");
			return false;
		} catch (MalformedJwtException e) {
			System.out.println("malfunction token");
			return false;
		}catch (MissingClaimException e) {
			System.out.println("missing subject in token");
			return false;
		} catch (IncorrectClaimException e) {
			System.out.println("incorrect subject token");
			return false;
		    
		}

	}
	public static boolean verifyPrivate(String token, Player ent) {
		try {

			 Jwts.parser().requireSubject(ent.getName())
					.setSigningKey(DatatypeConverter.parseBase64Binary(ent.getPublicKey()))
					.parseClaimsJws(token).getBody();
		      return true ;
		} catch (SignatureException e) {
			System.out.println("cant trust token");
			return false;

		} catch (ExpiredJwtException e) {
			System.out.println("expired token");
			return false;
		} catch (MalformedJwtException e) {
			System.out.println("malfunction token");
			return false;
		}catch (MissingClaimException e) {
			System.out.println("missing subject in token");
			return false;
		} catch (IncorrectClaimException e) {
			System.out.println("incorrect subject token");
			return false;
		    
		}

	}
	
	public static String getPrivateKey(Player ent, Date expdate) {

		apiKeySecretBytes = DatatypeConverter.parseBase64Binary(ent.getPublicKey());
		signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder().setId(ent.getPublicKey()).setSubject(ent.getName()).setIssuer("icryptopoker")
				.signWith(SignatureAlgorithm.HS512, signingKey);

		builder.setExpiration(expdate);

		return builder.compact();

	}
	
	

}
